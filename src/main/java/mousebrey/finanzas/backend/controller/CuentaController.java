package mousebrey.finanzas.backend.controller;

import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.CuentaRequest;
import mousebrey.finanzas.backend.model.request.CuentaUpdateRequest;
import mousebrey.finanzas.backend.model.response.CuentaResponse;
import mousebrey.finanzas.backend.service.CuentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<ResponseClient<CuentaResponse>> registrar(@RequestBody CuentaRequest request) {
        return ResponseEntity.ok(cuentaService.registrar(request));
    }

    @PutMapping
    public ResponseEntity<ResponseClient<CuentaResponse>> actualizar(@RequestBody CuentaUpdateRequest request) {
        return ResponseEntity.ok(cuentaService.actualizar(request));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ResponseClientList<CuentaResponse>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(cuentaService.listarPorUsuario(idUsuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseClient<CuentaResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.obtenerPorId(id));
    }
}

