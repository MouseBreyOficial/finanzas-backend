package mousebrey.finanzas.backend.service.impl;

import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.constante.Constant;
import mousebrey.finanzas.backend.domain.Cuenta;
import mousebrey.finanzas.backend.domain.Usuario;
import mousebrey.finanzas.backend.exception.ValidationException;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.CuentaRequest;
import mousebrey.finanzas.backend.model.request.CuentaUpdateRequest;
import mousebrey.finanzas.backend.model.response.CuentaResponse;
import mousebrey.finanzas.backend.repository.CuentaRepository;
import mousebrey.finanzas.backend.repository.UsuarioRepository;
import mousebrey.finanzas.backend.service.CuentaService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public ResponseClient<CuentaResponse> registrar(CuentaRequest request) {
        try {
            Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                    .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Usuario no encontrado"));

            Cuenta cuenta = new Cuenta();
            cuenta.setNombreCuenta(request.getNombreCuenta());
            java.math.BigDecimal inicial = request.getSaldoInicial() != null ? request.getSaldoInicial() : request.getSaldoActual();
            cuenta.setSaldoInicial(inicial);
            cuenta.setSaldoActual(inicial);
            cuenta.setUsuario(usuario);
            cuenta.setUsuarioCreacion(request.getUsuarioCreacion());
            cuenta.setFechaCreacion(LocalDateTime.now());

            cuentaRepository.save(cuenta);

            return ResponseClient.setOk(mapToResponse(cuenta));
        } catch (Exception e) {
            throw new ValidationException(Constant.CODIGO_ERROR, Constant.MENSAJE_ERROR);
        }
    }

    @Override
    public ResponseClient<CuentaResponse> actualizar(CuentaUpdateRequest request) {
        try {
            Cuenta cuenta = cuentaRepository.findById(request.getId())
                    .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Cuenta no encontrada"));

            cuenta.setNombreCuenta(request.getNombreCuenta());
            cuenta.setSaldoActual(request.getSaldoActual());
            cuenta.setUsuarioModificacion(request.getUsuarioModificacion());
            cuenta.setFechaModificacion(LocalDateTime.now());

            cuentaRepository.save(cuenta);

            return ResponseClient.setOk(mapToResponse(cuenta));
        } catch (Exception e) {
            throw new ValidationException(Constant.CODIGO_ERROR, Constant.MENSAJE_ERROR);
        }
    }

    @Override
    public ResponseClientList<CuentaResponse> listarPorUsuario(Long idUsuario) {
        List<CuentaResponse> lista = cuentaRepository.findByUsuarioIdUsuario(idUsuario).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseClientList.setOk(lista);
    }

    @Override
    public ResponseClient<CuentaResponse> obtenerPorId(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Cuenta no encontrada"));
        return ResponseClient.setOk(mapToResponse(cuenta));

    }

    private CuentaResponse mapToResponse(Cuenta cuenta) {
        CuentaResponse response = new CuentaResponse();
        response.setId(cuenta.getIdCuenta());
        response.setNombreCuenta(cuenta.getNombreCuenta());
        response.setSaldoActual(cuenta.getSaldoActual());
        response.setSaldoInicial(cuenta.getSaldoInicial());
        response.setIdUsuario(cuenta.getUsuario().getIdUsuario());
        return response;
    }
}

