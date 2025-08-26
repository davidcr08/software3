package uniquindio.product.services.implementations;

import uniquindio.product.enums.EstadoCuenta;
import uniquindio.product.model.Cliente;

import uniquindio.product.repositories.ClienteRepository;
import uniquindio.product.services.interfaces.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public Cliente crearCliente(Cliente cliente) {
        if (clienteRepository.existsByCedula(cliente.getCedula())) {
            throw new RuntimeException("Ya existe un cliente con esa cédula");
        }

        if (clienteRepository.existsByCorreoElectronico(cliente.getCorreoElectronico())) {
            throw new RuntimeException("Ya existe un cliente con ese email");
        }

        cliente.setEstadoCuenta(EstadoCuenta.ACTIVO);
        return clienteRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> obtenerCliente(String id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("No existe un cliente con ese ID");
        }
        return clienteRepository.findById(id);
    }

    @Override
    public Optional<Cliente> obtenerClientePorCedula(String cedula) {
        if (!clienteRepository.existsByCedula(cedula)) {
            throw new RuntimeException("No existe un cliente con esa cédula");
        }
        return clienteRepository.findByCedula(cedula);
    }

    @Override
    public Optional<Cliente> obtenerClientePorEmail(String email) {
        if (!clienteRepository.existsByCorreoElectronico(email)) {
            throw new RuntimeException("No existe un cliente con ese email");
        }
        return clienteRepository.findByCorreoElectronico(email);
    }

    @Override
    public Cliente actualizarCliente(Cliente cliente) {
        if (!clienteRepository.existsById(cliente.getId())) {
            throw new RuntimeException("No existe un cliente con ese ID");
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminarCliente(String id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un cliente con ese ID"));
        cliente.setEstadoCuenta(EstadoCuenta.INACTIVO);
        clienteRepository.save(cliente);
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }
}