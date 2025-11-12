package com.tp_bda.inventario.controller;

import com.tp_bda.inventario.dto.CamionDTO;
import com.tp_bda.inventario.service.CamionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.math.BigDecimal;

@RestController
@RequestMapping("/camiones")
public class CamionController {

    @Autowired
    private CamionService camionService;

    @PostMapping
    public ResponseEntity<CamionDTO> crearCamion(@RequestBody CamionDTO dto) {
        CamionDTO nuevo = camionService.guardar(dto);
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CamionDTO> obtenerPorId(@PathVariable Integer id) {
        CamionDTO dto = camionService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/{id}/validar")
    public ResponseEntity<Boolean> validarCapacidad(
            @PathVariable Integer id,
            @RequestParam BigDecimal peso,
            @RequestParam BigDecimal volumen) {

        // 1. La lógica de validación está en el servicio.
        // 2. El servicio arrojará una excepción (404 o 409) si no es válido.
        camionService.validarCapacidad(id, peso, volumen);

        // 3. Si el servicio NO arroja una excepción, el camión es apto.
        // El cliente (InventarioClient) recibirá esto (HTTP 200 OK, body: true)
        // y su .blockOptional() devolverá true.
        return ResponseEntity.ok(true);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CamionDTO> actualizarCamion(@PathVariable Integer id, @RequestBody CamionDTO dto) {
        CamionDTO actualizado = camionService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping
    public List<CamionDTO> listarCamiones() {
        return camionService.listar();
    }
}
