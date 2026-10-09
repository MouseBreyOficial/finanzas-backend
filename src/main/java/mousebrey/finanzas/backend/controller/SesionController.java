package mousebrey.finanzas.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mousebrey.finanzas.backend.model.AuthRequest;
import mousebrey.finanzas.backend.model.AuthResponse;
import mousebrey.finanzas.backend.model.RegistroDto;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.service.SesionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class SesionController {

    private final SesionService sesionServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<ResponseClient<AuthResponse>> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(sesionServiceImpl.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseClient<AuthResponse>> registrar(@RequestBody RegistroDto dto) {
        return ResponseEntity.ok(sesionServiceImpl.registrar(dto));
    }

    @GetMapping("/backend/up")
    public ResponseEntity<String> wakeup() {
        log.info("Backend activo...");
        return ResponseEntity.ok("Backend activo");
    }
}
