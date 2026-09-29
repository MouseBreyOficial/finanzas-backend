package mousebrey.finanzas.backend.controller;

import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.GastoRequest;
import mousebrey.finanzas.backend.model.request.GastoUpdateRequest;
import mousebrey.finanzas.backend.model.response.GastoResponse;
import mousebrey.finanzas.backend.service.GastoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService gastoService;

    @PostMapping
    public ResponseEntity<ResponseClient<GastoResponse>> registrar(@RequestBody GastoRequest request) {
        return ResponseEntity.ok(gastoService.registrar(request));
    }

    @PutMapping
    public ResponseEntity<ResponseClient<GastoResponse>> actualizar(@RequestBody GastoUpdateRequest request) {
        return ResponseEntity.ok(gastoService.actualizar(request));
    }

    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<ResponseClientList<GastoResponse>> listarPorCuenta(@PathVariable Long idCuenta) {
        return ResponseEntity.ok(gastoService.listarPorCuenta(idCuenta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseClient<GastoResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(gastoService.obtenerPorId(id));
    }

    @GetMapping("/promedios/{idUsuario}")
    public ResponseEntity<ResponseClient<Map<String, Double>>> promedios(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(gastoService.promedioPorCategoria(idUsuario));
    }
    @GetMapping("/categorias/{idUsuario}")
    public ResponseEntity<ResponseClientList<String>> categorias(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(gastoService.categorias(idUsuario));
    }

    @GetMapping("/resumen/{idUsuario}")
    public ResponseEntity<ResponseClient<Map<String, Object>>> resumen(@PathVariable Long idUsuario, @RequestParam LocalDate desde, @RequestParam LocalDate hasta) {
        return ResponseEntity.ok(gastoService.resumen(idUsuario, desde, hasta));
    }
}

