package mousebrey.finanzas.backend.service.impl;

import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.constante.Constant;
import mousebrey.finanzas.backend.domain.Cuenta;
import mousebrey.finanzas.backend.domain.Gasto;
import mousebrey.finanzas.backend.exception.ValidationException;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.GastoRequest;
import mousebrey.finanzas.backend.model.request.GastoUpdateRequest;
import mousebrey.finanzas.backend.model.response.GastoResponse;
import mousebrey.finanzas.backend.repository.CuentaRepository;
import mousebrey.finanzas.backend.repository.GastoRepository;
import mousebrey.finanzas.backend.service.GastoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GastoServiceImpl implements GastoService {

    private final GastoRepository gastoRepository;
    private final CuentaRepository cuentaRepository;

    @Override
    public ResponseClient<GastoResponse> registrar(GastoRequest request) {
        Cuenta cuenta = cuentaRepository.findById(request.getIdCuenta())
                .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Cuenta no encontrada"));

        BigDecimal disponible = cuenta.getSaldoActual() == null ? BigDecimal.ZERO : cuenta.getSaldoActual();

        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException(Constant.CODIGO_ERROR, "El monto debe ser mayor a cero");

        if (request.getMonto().compareTo(disponible) > 0)
            throw new ValidationException(Constant.CODIGO_ERROR, "Saldo insuficiente. Disponible: S/ " + disponible);

        Gasto gasto = new Gasto();
        gasto.setMonto(request.getMonto());
        gasto.setFecha(request.getFecha());
        gasto.setCategoria(normalizarCategoria(request.getCategoria()));
        gasto.setDescripcion(request.getDescripcion());
        gasto.setCuenta(cuenta);
        gasto.setUsuarioCreacion(request.getUsuarioCreacion());
        gasto.setFechaCreacion(LocalDateTime.now());
        gastoRepository.save(gasto);

        cuenta.setSaldoActual(disponible.subtract(request.getMonto()));
        cuentaRepository.save(cuenta);

        return ResponseClient.setOk(mapToResponse(gasto));
    }

    @Override
    public ResponseClient<GastoResponse> actualizar(GastoUpdateRequest request) {
        Gasto gasto = gastoRepository.findById(request.getId())
                .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Gasto no encontrado"));

        Cuenta cuenta = gasto.getCuenta();
        BigDecimal montoAnterior = gasto.getMonto();
        BigDecimal disponibleReal = (cuenta.getSaldoActual() == null ? BigDecimal.ZERO : cuenta.getSaldoActual()).add(montoAnterior);

        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException(Constant.CODIGO_ERROR, "El monto debe ser mayor a cero");

        if (request.getMonto().compareTo(disponibleReal) > 0)
            throw new ValidationException(Constant.CODIGO_ERROR, "Saldo insuficiente. Disponible para este gasto: S/ " + disponibleReal);

        gasto.setMonto(request.getMonto());
        gasto.setFecha(request.getFecha());
        gasto.setCategoria(normalizarCategoria(request.getCategoria()));
        gasto.setDescripcion(request.getDescripcion());
        gasto.setUsuarioModificacion(request.getUsuarioModificacion());
        gasto.setFechaModificacion(LocalDateTime.now());
        gastoRepository.save(gasto);

        cuenta.setSaldoActual(disponibleReal.subtract(request.getMonto()));
        cuentaRepository.save(cuenta);

        return ResponseClient.setOk(mapToResponse(gasto));
    }

    @Override
    public ResponseClientList<GastoResponse> listarPorCuenta(Long idCuenta) {
        List<GastoResponse> lista = gastoRepository.findByCuentaIdCuenta(idCuenta).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseClientList.setOk(lista);
    }

    @Override
    public ResponseClient<GastoResponse> obtenerPorId(Long id) {
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Gasto no encontrado"));
        return ResponseClient.setOk(mapToResponse(gasto));
    }

    @Override
    public ResponseClient<Map<String, Double>> promedioPorCategoria(Long idUsuario) {
        Map<String, Double> resultado = new HashMap<>();
        for(Object[] fila : gastoRepository.promedioPorCategoria(idUsuario)) {
            resultado.put((String) fila[0], ((Double) fila[1]));
        }
        return ResponseClient.setOk(resultado);
    }


    @Override
    public ResponseClient<Map<String, Object>> resumen(Long idUsuario, LocalDate desde, LocalDate hasta) {
        List<Gasto> gastos = gastoRepository.findResumen(idUsuario, desde, hasta);
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("total", gastos.stream().map(Gasto::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add));
        Map<String, BigDecimal> porDia = new LinkedHashMap<>();
        Map<String, BigDecimal> porCategoria = new LinkedHashMap<>();
        gastos.forEach(g -> {
            porDia.merge(g.getFecha().toString(), g.getMonto(), BigDecimal::add);
            porCategoria.merge(g.getCategoria(), g.getMonto(), BigDecimal::add);
        });
        r.put("porDia", porDia); r.put("porCategoria", porCategoria); r.put("cantidad", gastos.size());
        r.put("detalles", gastos.stream().map(this::mapToResponse).collect(Collectors.toList()));
        return ResponseClient.setOk(r);
    }

    @Override
    public ResponseClientList<String> categorias(Long idUsuario) {
        return ResponseClientList.setOk(gastoRepository.categoriasPorUsuario(idUsuario));
    }

    private String normalizarCategoria(String categoria) {
        if (categoria == null) return null;
        String v = categoria.trim().replaceAll("\\s+", " ");
        if (v.isEmpty()) return v;
        return v.substring(0, 1).toUpperCase() + v.substring(1).toLowerCase();
    }

    private GastoResponse mapToResponse(Gasto gasto) {
        GastoResponse response = new GastoResponse();
        response.setId(gasto.getIdGasto());
        response.setMonto(gasto.getMonto());
        response.setFecha(gasto.getFecha());
        response.setCategoria(gasto.getCategoria());
        response.setDescripcion(gasto.getDescripcion());
        response.setIdCuenta(gasto.getCuenta().getIdCuenta());
        return response;
    }
}
