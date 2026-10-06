package mousebrey.finanzas.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.request.PushSuscripcionEliminarRequest;
import mousebrey.finanzas.backend.model.request.PushSuscripcionRequest;
import mousebrey.finanzas.backend.service.PushSuscripcionService;
import mousebrey.finanzas.backend.service.WebPushService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/push")
@RequiredArgsConstructor
public class PushSuscripcionController {

    private final PushSuscripcionService pushSuscripcionService;
    private final WebPushService webPushService;

    @PostMapping("/suscribir/{idUsuario}")
    public ResponseEntity<ResponseClient<Void>> suscribir(
            @PathVariable Long idUsuario,
            @Valid @RequestBody PushSuscripcionRequest request) {

        return ResponseEntity.ok(pushSuscripcionService.registrarSuscripcion(
                idUsuario, request));
    }

    @PostMapping("/desuscribir/{idUsuario}")
    public ResponseEntity<ResponseClient<Void>> desuscribir(
            @PathVariable Long idUsuario,
            @Valid @RequestBody PushSuscripcionEliminarRequest request) {

        return ResponseEntity.ok(pushSuscripcionService.eliminarSuscripcion(
                idUsuario, request.getEndpoint()));
    }

    @GetMapping("/estado/{idUsuario}")
    public ResponseEntity<ResponseClient<Map<String, Boolean>>> estado(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(pushSuscripcionService.estado(idUsuario));
    }

    @PostMapping("/prueba/{idUsuario}")
    public ResponseEntity<ResponseClient<Void>> prueba(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(webPushService.enviarPrueba(idUsuario));
    }
}

