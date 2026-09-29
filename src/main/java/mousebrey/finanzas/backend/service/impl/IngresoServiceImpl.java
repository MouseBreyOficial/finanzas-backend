package mousebrey.finanzas.backend.service.impl;

import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.constante.Constant;
import mousebrey.finanzas.backend.domain.Cuenta;
import mousebrey.finanzas.backend.domain.Ingreso;
import mousebrey.finanzas.backend.exception.ValidationException;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.IngresoRequest;
import mousebrey.finanzas.backend.model.request.IngresoUpdateRequest;
import mousebrey.finanzas.backend.model.response.IngresoResponse;
import mousebrey.finanzas.backend.repository.CuentaRepository;
import mousebrey.finanzas.backend.repository.IngresoRepository;
import mousebrey.finanzas.backend.service.IngresoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngresoServiceImpl implements IngresoService {

    private final IngresoRepository ingresoRepository;
    private final CuentaRepository cuentaRepository;

    @Override
    public ResponseClient<IngresoResponse> registrar(IngresoRequest request) {
        try {
            Cuenta cuenta = cuentaRepository.findById(request.getIdCuenta())
                    .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Cuenta no encontrada"));

            Ingreso ingreso = new Ingreso();
            ingreso.setMonto(request.getMonto());
            ingreso.setFecha(request.getFecha());
            ingreso.setDescripcion(request.getDescripcion());
            ingreso.setCuenta(cuenta);
            ingreso.setUsuarioCreacion(request.getUsuarioCreacion());
            ingreso.setFechaCreacion(LocalDateTime.now());

            ingresoRepository.save(ingreso);
            cuenta.setSaldoActual((cuenta.getSaldoActual() == null ? java.math.BigDecimal.ZERO : cuenta.getSaldoActual()).add(request.getMonto()));
            cuentaRepository.save(cuenta);

            return ResponseClient.setOk(mapToResponse(ingreso));
        } catch (Exception e) {
            throw new ValidationException(Constant.CODIGO_ERROR, Constant.MENSAJE_ERROR);
        }
    }

    @Override
    public ResponseClient<IngresoResponse> actualizar(IngresoUpdateRequest request) {
        try {
            Ingreso ingreso = ingresoRepository.findById(request.getId())
                    .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Ingreso no encontrado"));

            java.math.BigDecimal montoAnterior = ingreso.getMonto();
            ingreso.setMonto(request.getMonto());
            ingreso.setFecha(request.getFecha());
            ingreso.setDescripcion(request.getDescripcion());
            ingreso.setUsuarioModificacion(request.getUsuarioModificacion());
            ingreso.setFechaModificacion(LocalDateTime.now());

            ingresoRepository.save(ingreso);
            Cuenta cuenta = ingreso.getCuenta();
            cuenta.setSaldoActual((cuenta.getSaldoActual() == null ? java.math.BigDecimal.ZERO : cuenta.getSaldoActual()).add(request.getMonto().subtract(montoAnterior)));
            cuentaRepository.save(cuenta);

            return ResponseClient.setOk(mapToResponse(ingreso));
        } catch (Exception e) {
            throw new ValidationException(Constant.CODIGO_ERROR, Constant.MENSAJE_ERROR);
        }
    }

    @Override
    public ResponseClientList<IngresoResponse> listarPorCuenta(Long idCuenta) {
        List<IngresoResponse> lista = ingresoRepository.findByCuentaIdCuenta(idCuenta).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseClientList.setOk(lista);
    }

    @Override
    public ResponseClient<IngresoResponse> obtenerPorId(Long id) {
        Ingreso ingreso = ingresoRepository.findById(id)
                .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Ingreso no encontrado"));
        return ResponseClient.setOk(mapToResponse(ingreso));
    }

    private IngresoResponse mapToResponse(Ingreso ingreso) {
        IngresoResponse response = new IngresoResponse();
        response.setId(ingreso.getIdIngreso());
        response.setMonto(ingreso.getMonto());
        response.setFecha(ingreso.getFecha());
        response.setDescripcion(ingreso.getDescripcion());
        response.setIdCuenta(ingreso.getCuenta().getIdCuenta());
        return response;
    }
}

