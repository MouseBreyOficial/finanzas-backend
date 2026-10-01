package mousebrey.finanzas.backend.controller;

import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.AlertaRequest;
import mousebrey.finanzas.backend.model.request.AlertaUpdateRequest;
import mousebrey.finanzas.backend.model.response.AlertaResponse;
import mousebrey.finanzas.backend.service.AlertaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
public class AlertaController {

    private final AlertaService alertaService;

    @PostMapping
    public ResponseEntity<ResponseClient<AlertaResponse>> registrar(@RequestBody AlertaRequest request) {
        return ResponseEntity.ok(alertaService.registrar(request));
    }

    @PutMapping
    public ResponseEntity<ResponseClient<AlertaResponse>> actualizar(@RequestBody AlertaUpdateRequest request) {
        return ResponseEntity.ok(alertaService.actualizar(request));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ResponseClientList<AlertaResponse>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(alertaService.listarPorUsuario(idUsuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseClient<AlertaResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alertaService.obtenerPorId(id));
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<ResponseClient<AlertaResponse>> marcarPagada(@PathVariable Long id, @RequestParam String usuario) {
        return ResponseEntity.ok(alertaService.marcarPagada(id, usuario));
    }
}

