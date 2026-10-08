package mousebrey.finanzas.backend.service.impl;

import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.constante.Constant;
import mousebrey.finanzas.backend.domain.Cuenta;
import mousebrey.finanzas.backend.domain.Transferencia;
import mousebrey.finanzas.backend.domain.Usuario;
import mousebrey.finanzas.backend.exception.ValidationException;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.TransferenciaRequest;
import mousebrey.finanzas.backend.model.response.TransferenciaResponse;
import mousebrey.finanzas.backend.repository.CuentaRepository;
import mousebrey.finanzas.backend.repository.TransferenciaRepository;
import mousebrey.finanzas.backend.repository.UsuarioRepository;
import mousebrey.finanzas.backend.service.TransferenciaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferenciaServiceImpl implements TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final CuentaRepository cuentaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public ResponseClient<TransferenciaResponse> registrar(TransferenciaRequest request) {
        if (request.getIdUsuario() == null)
            throw new ValidationException(Constant.CODIGO_ERROR, "El usuario es obligatorio");

        if (request.getIdCuentaOrigen() == null || request.getIdCuentaDestino() == null)
            throw new ValidationException(Constant.CODIGO_ERROR, "Debe seleccionar la cuenta de origen y destino");

        if (request.getIdCuentaOrigen().equals(request.getIdCuentaDestino()))
            throw new ValidationException(Constant.CODIGO_ERROR, "La cuenta de origen y destino deben ser diferentes");

        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException(Constant.CODIGO_ERROR, "El monto debe ser mayor a cero");

        if (request.getFechaTransferencia() == null)
            throw new ValidationException(Constant.CODIGO_ERROR, "La fecha de transferencia es obligatoria");

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Usuario no encontrado"));

        Cuenta origen = cuentaRepository.findById(request.getIdCuentaOrigen())
                .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Cuenta de origen no encontrada"));

        Cuenta destino = cuentaRepository.findById(request.getIdCuentaDestino())
                .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Cuenta de destino no encontrada"));

        if (!origen.getUsuario().getIdUsuario().equals(request.getIdUsuario()) ||
                !destino.getUsuario().getIdUsuario().equals(request.getIdUsuario())) {
            throw new ValidationException(Constant.CODIGO_ERROR, "Las cuentas seleccionadas no pertenecen al usuario");
        }

        BigDecimal saldoOrigen = origen.getSaldoActual() == null ? BigDecimal.ZERO : origen.getSaldoActual();
        BigDecimal saldoDestino = destino.getSaldoActual() == null ? BigDecimal.ZERO : destino.getSaldoActual();

        if (request.getMonto().compareTo(saldoOrigen) > 0)
            throw new ValidationException(Constant.CODIGO_ERROR, "Saldo insuficiente. Disponible: S/ " + saldoOrigen);

        origen.setSaldoActual(saldoOrigen.subtract(request.getMonto()));
        origen.setUsuarioModificacion(request.getUsuarioCreacion());
        origen.setFechaModificacion(LocalDateTime.now());

        destino.setSaldoActual(saldoDestino.add(request.getMonto()));
        destino.setUsuarioModificacion(request.getUsuarioCreacion());
        destino.setFechaModificacion(LocalDateTime.now());

        cuentaRepository.save(origen);
        cuentaRepository.save(destino);

        Transferencia transferencia = new Transferencia();
        transferencia.setUsuario(usuario);
        transferencia.setCuentaOrigen(origen);
        transferencia.setCuentaDestino(destino);
        transferencia.setMonto(request.getMonto());
        transferencia.setFechaTransferencia(request.getFechaTransferencia());
        transferencia.setDescripcion(request.getDescripcion());
        transferencia.setUsuarioCreacion(request.getUsuarioCreacion());
        transferencia.setFechaCreacion(LocalDateTime.now());

        transferenciaRepository.save(transferencia);

        return ResponseClient.setOk(mapToResponse(transferencia));
    }

    @Override
    public ResponseClientList<TransferenciaResponse> listarPorUsuario(Long idUsuario) {
        List<TransferenciaResponse> lista = transferenciaRepository
                .findByUsuarioIdUsuarioOrderByFechaTransferenciaDescIdTransferenciaDesc(idUsuario)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseClientList.setOk(lista);
    }

    @Override
    public ResponseClient<TransferenciaResponse> obtenerPorId(Long id) {
        Transferencia transferencia = transferenciaRepository.findById(id)
                .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Transferencia no encontrada"));
        return ResponseClient.setOk(mapToResponse(transferencia));
    }

    private TransferenciaResponse mapToResponse(Transferencia transferencia) {
        TransferenciaResponse response = new TransferenciaResponse();
        response.setId(transferencia.getIdTransferencia());
        response.setIdUsuario(transferencia.getUsuario().getIdUsuario());
        response.setIdCuentaOrigen(transferencia.getCuentaOrigen().getIdCuenta());
        response.setNombreCuentaOrigen(transferencia.getCuentaOrigen().getNombreCuenta());
        response.setIdCuentaDestino(transferencia.getCuentaDestino().getIdCuenta());
        response.setNombreCuentaDestino(transferencia.getCuentaDestino().getNombreCuenta());
        response.setMonto(transferencia.getMonto());
        response.setFechaTransferencia(transferencia.getFechaTransferencia());
        response.setDescripcion(transferencia.getDescripcion());
        return response;
    }
}
