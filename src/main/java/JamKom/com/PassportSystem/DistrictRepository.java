package JamKom.com.PassportSystem;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<District, Integer>{

	Optional<District> findById(Long resDistrict);
	

}
