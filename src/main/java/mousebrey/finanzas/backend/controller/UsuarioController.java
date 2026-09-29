package mousebrey.finanzas.backend.controller;

import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.UsuarioDeleteRequest;
import mousebrey.finanzas.backend.model.request.UsuarioRequest;
import mousebrey.finanzas.backend.model.request.UsuarioUpdateRequest;
import mousebrey.finanzas.backend.model.response.UsuarioResponse;
import mousebrey.finanzas.backend.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseClient<UsuarioResponse> registrar(@RequestBody UsuarioRequest request) {
        return usuarioService.registrar(request);
    }

    @PostMapping("/actualizar")
    public ResponseClient<UsuarioResponse> actualizar(@RequestBody UsuarioUpdateRequest request) {
        return usuarioService.actualizar(request);
    }

    @PostMapping("/eliminar")
    public ResponseClient<Void> eliminar(@RequestBody UsuarioDeleteRequest request) {
        return usuarioService.eliminar(request);
    }

    @PostMapping("/{id}/activar")
    public ResponseClient<Void> activar(@PathVariable Long id, @RequestParam String usuario) {
        return usuarioService.activar(id, usuario);
    }

    @GetMapping("/listar")
    public ResponseClientList<UsuarioResponse> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseClient<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }
}
