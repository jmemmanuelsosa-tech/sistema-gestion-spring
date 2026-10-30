package com.sistema.gestion.service;
import com.sistema.gestion.model.Cliente;
import com.sistema.gestion.repository.ClienteRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author sosap
 */
@Service
public class ClienteService {
  private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró ningún cliente con el ID: " + id));
    }

    public Cliente guardarCliente(Cliente cliente) {
        // Validaciones obligatorias (Nombre y Apellido)
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }
        if (cliente.getApellido() == null || cliente.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido del cliente es obligatorio.");
        }

        // Validaciones opcionales: Email
        if (cliente.getEmail() != null && !cliente.getEmail().isBlank()) {
            if (!cliente.getEmail().contains("@")) {
                throw new IllegalArgumentException("Si ingresa un correo, debe ser válido (contener '@').");
            }
            if (clienteRepository.existsByEmail(cliente.getEmail())) {
                throw new IllegalArgumentException("Ya existe un cliente registrado con el email: " + cliente.getEmail());
            }
        } else {
            // Si viene en blanco, lo forzamos a null.
            // Esto evita que MySQL tire error de "email duplicado" si hay varios clientes sin email.
            cliente.setEmail(null);
        }

        // Validaciones opcionales: DNI
        if (cliente.getDni() != null && !cliente.getDni().isBlank()) {
            if (clienteRepository.existsByDni(cliente.getDni())) {
                throw new IllegalArgumentException("Ya existe un cliente registrado con el DNI: " + cliente.getDni());
            }
        } else {
            cliente.setDni(null);
        }

        return clienteRepository.save(cliente);
    }

    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar: El cliente con ID " + id + " no existe.");
        }
        clienteRepository.deleteById(id);
    }
}
