package mousebrey.finanzas.backend.service.impl;

import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.constante.Constant;
import mousebrey.finanzas.backend.domain.Alerta;
import mousebrey.finanzas.backend.domain.Cuenta;
import mousebrey.finanzas.backend.domain.Gasto;
import mousebrey.finanzas.backend.repository.GastoRepository;
import mousebrey.finanzas.backend.repository.CuentaRepository;
import mousebrey.finanzas.backend.domain.Usuario;
import mousebrey.finanzas.backend.exception.ValidationException;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.AlertaRequest;
import mousebrey.finanzas.backend.model.request.AlertaUpdateRequest;
import mousebrey.finanzas.backend.model.response.AlertaResponse;
import mousebrey.finanzas.backend.repository.AlertaRepository;
import mousebrey.finanzas.backend.repository.UsuarioRepository;
import mousebrey.finanzas.backend.service.AlertaService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertaServiceImpl implements AlertaService {

    private final AlertaRepository alertaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;
    private final GastoRepository gastoRepository;

    @Override
    public ResponseClient<AlertaResponse> registrar(AlertaRequest request) {
        try {
            Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                    .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Usuario no encontrado"));

            Alerta alerta = new Alerta();
            alerta.setDescripcion(request.getDescripcion());
            alerta.setFechaAlerta(request.getFechaAlerta());
            alerta.setTipo(request.getTipo());
            alerta.setEsRecurrente(Boolean.TRUE.equals(request.getEsRecurrente()));
            alerta.setFrecuencia(request.getFrecuencia());
            alerta.setDiaMes(request.getDiaMes());
            alerta.setEstado(request.getEstado() == null ? "PENDIENTE" : request.getEstado());
            alerta.setMonto(request.getMonto());
            alerta.setCategoria(request.getCategoria());
            alerta.setCuenta(request.getIdCuenta() == null ? null : cuentaRepository.findById(request.getIdCuenta()).orElse(null));
            alerta.setUsuario(usuario);
            alerta.setUsuarioCreacion(request.getUsuarioCreacion());
            alerta.setFechaCreacion(LocalDateTime.now());

            alertaRepository.save(alerta);

            return ResponseClient.setOk(mapToResponse(alerta));
        } catch (Exception e) {
            throw new ValidationException(Constant.CODIGO_ERROR, Constant.MENSAJE_ERROR);
        }
    }

    @Override
    public ResponseClient<AlertaResponse> actualizar(AlertaUpdateRequest request) {
        try {
            Alerta alerta = alertaRepository.findById(request.getId())
                    .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Alerta no encontrada"));

            alerta.setDescripcion(request.getDescripcion());
            alerta.setFechaAlerta(request.getFechaAlerta());
            alerta.setTipo(request.getTipo());
            alerta.setEsRecurrente(Boolean.TRUE.equals(request.getEsRecurrente()));
            alerta.setFrecuencia(request.getFrecuencia());
            alerta.setDiaMes(request.getDiaMes());
            alerta.setEstado(request.getEstado() == null ? "PENDIENTE" : request.getEstado());
            alerta.setMonto(request.getMonto());
            alerta.setCategoria(request.getCategoria());
            alerta.setCuenta(request.getIdCuenta() == null ? null : cuentaRepository.findById(request.getIdCuenta()).orElse(null));
            alerta.setUsuarioModificacion(request.getUsuarioModificacion());
            alerta.setFechaModificacion(LocalDateTime.now());

            alertaRepository.save(alerta);

            return ResponseClient.setOk(mapToResponse(alerta));
        } catch (Exception e) {
            throw new ValidationException(Constant.CODIGO_ERROR, Constant.MENSAJE_ERROR);
        }
    }

    @Override
    public ResponseClientList<AlertaResponse> listarPorUsuario(Long idUsuario) {
        List<AlertaResponse> lista = alertaRepository.findByUsuarioIdUsuario(idUsuario).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseClientList.setOk(lista);
    }

    @Override
    public ResponseClient<AlertaResponse> obtenerPorId(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Alerta no encontrada"));
        return ResponseClient.setOk(mapToResponse(alerta));
    }


    @Override
    public ResponseClient<AlertaResponse> marcarPagada(Long id, String usuario) {
        Alerta alerta = alertaRepository.findById(id).orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Alerta no encontrada"));
        if (alerta.getMonto() != null && alerta.getCuenta() != null) {
            Cuenta cuenta = alerta.getCuenta();
            BigDecimal disponible = cuenta.getSaldoActual() == null ? BigDecimal.ZERO : cuenta.getSaldoActual();
            if (alerta.getMonto().compareTo(disponible) > 0) {
                throw new ValidationException(Constant.CODIGO_ERROR, "Saldo insuficiente para pagar la alerta. Disponible: S/ " + disponible);
            }
            Gasto gasto = new Gasto();
            gasto.setMonto(alerta.getMonto()); gasto.setFecha(LocalDate.now());
            gasto.setCategoria(alerta.getCategoria() == null ? "PAGO_FIJO" : alerta.getCategoria());
            gasto.setDescripcion(alerta.getDescripcion()); gasto.setCuenta(alerta.getCuenta());
            gasto.setUsuarioCreacion(usuario); gasto.setFechaCreacion(LocalDateTime.now()); gastoRepository.save(gasto);
            cuenta.setSaldoActual(disponible.subtract(alerta.getMonto()));
            cuentaRepository.save(cuenta);
        }
        if (Boolean.TRUE.equals(alerta.getEsRecurrente())) {
            LocalDate base = alerta.getFechaAlerta().isAfter(LocalDate.now()) ? alerta.getFechaAlerta() : LocalDate.now();
            LocalDate next = base.plusMonths(1);
            int dia = alerta.getDiaMes() == null ? alerta.getFechaAlerta().getDayOfMonth() : alerta.getDiaMes();
            alerta.setFechaAlerta(next.withDayOfMonth(Math.min(dia, next.lengthOfMonth())));
            alerta.setEstado("PENDIENTE");
        } else {
            alerta.setEstado("PAGADO");
        }
        alerta.setUsuarioModificacion(usuario);
        alerta.setFechaModificacion(LocalDateTime.now());
        alertaRepository.save(alerta);
        return ResponseClient.setOk(mapToResponse(alerta));
    }

    private AlertaResponse mapToResponse(Alerta alerta) {
        AlertaResponse response = new AlertaResponse();
        response.setId(alerta.getIdAlerta());
        response.setDescripcion(alerta.getDescripcion());
        response.setFechaAlerta(alerta.getFechaAlerta());
        response.setTipo(alerta.getTipo());
        response.setEsRecurrente(alerta.getEsRecurrente());
        response.setFrecuencia(alerta.getFrecuencia());
        response.setDiaMes(alerta.getDiaMes());
        response.setEstado(alerta.getEstado());
        response.setMonto(alerta.getMonto());
        response.setCategoria(alerta.getCategoria());
        response.setIdCuenta(alerta.getCuenta() == null ? null : alerta.getCuenta().getIdCuenta());
        response.setIdUsuario(alerta.getUsuario().getIdUsuario());
        return response;
    }
}

