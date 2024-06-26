package tech.vault.server.application.service.strategy.count;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.vault.server.domain.entity.values.LocationComputer;
import tech.vault.server.domain.repository.ComputerRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ComputerCountLocationServiceImpl implements ComputerCountLocationService {
    @Autowired
    private ComputerRepository repository;

    /**
     * @return Map<LocationComputer, Long>
     * Busca dentro do banco os locais e a quantidade de computadores em cada local
     */
    @Override
    public Map<LocationComputer, Long> countComputerByLocation() {
        List<Object[]> result = repository.countComputersByLocation();

        Map<LocationComputer, Long> locationCountMap = new HashMap<>();
        for (Object[] response : result) {
            LocationComputer location = (LocationComputer) response[0];
            Long count = (Long) response[1];
            locationCountMap.put(location, count);
        }

        return locationCountMap;
    }
}
