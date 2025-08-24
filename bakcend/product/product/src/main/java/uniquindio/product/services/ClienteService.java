package uniquindio.product.services;

import uniquindio.product.model.Cliente;
import uniquindio.product.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> obtenerClientePorId(String id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> obtenerClientePorCedula(String cedula) {
        return clienteRepository.findByCedula(cedula);
    }

    public Optional<Cliente> obtenerClientePorEmail(String email) {
        return clienteRepository.findByCorreoElectronico(email);
    }

    public List<Cliente> obtenerTodosClientes() {
        return clienteRepository.findAll();
    }

    public void eliminarCliente(String id) {
        clienteRepository.deleteById(id);
    }

    public boolean existeClientePorCedula(String cedula) {
        return clienteRepository.existsByCedula(cedula);
    }

    public boolean existeClientePorEmail(String email) {
        return clienteRepository.existsByCorreoElectronico(email);
    }
}