package mousebrey.finanzas.backend.controller;

import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.IngresoRequest;
import mousebrey.finanzas.backend.model.request.IngresoUpdateRequest;
import mousebrey.finanzas.backend.model.response.IngresoResponse;
import mousebrey.finanzas.backend.service.IngresoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingresos")
@RequiredArgsConstructor
public class IngresoController {

    private final IngresoService ingresoService;

    @PostMapping
    public ResponseEntity<ResponseClient<IngresoResponse>> registrar(@RequestBody IngresoRequest request) {
        return ResponseEntity.ok(ingresoService.registrar(request));
    }

    @PutMapping
    public ResponseEntity<ResponseClient<IngresoResponse>> actualizar(@RequestBody IngresoUpdateRequest request) {
        return ResponseEntity.ok(ingresoService.actualizar(request));
    }

    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<ResponseClientList<IngresoResponse>> listarPorCuenta(@PathVariable Long idCuenta) {
        return ResponseEntity.ok(ingresoService.listarPorCuenta(idCuenta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseClient<IngresoResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ingresoService.obtenerPorId(id));
    }
}

