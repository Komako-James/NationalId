package JamKom.com.PassportSystem;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PassportRepository extends JpaRepository<PassportModel, Integer>{

	List<PassportModel> findBySurnameAndGivenNameAndDateOfBirth(String surname, String givenName, Date dob);
	


}
