package mousebrey.finanzas.backend.controller;

import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.TransferenciaRequest;
import mousebrey.finanzas.backend.model.response.TransferenciaResponse;
import mousebrey.finanzas.backend.service.TransferenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transferencias")
@RequiredArgsConstructor
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    @PostMapping
    public ResponseEntity<ResponseClient<TransferenciaResponse>> registrar(@RequestBody TransferenciaRequest request) {
        return ResponseEntity.ok(transferenciaService.registrar(request));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ResponseClientList<TransferenciaResponse>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(transferenciaService.listarPorUsuario(idUsuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseClient<TransferenciaResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transferenciaService.obtenerPorId(id));
    }
}
