package com.coviwin.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.coviwin.model.AdharCard;
import com.coviwin.model.IdCard;
import com.coviwin.model.PanCard;

@Repository
public interface IdCardServiceRepo extends JpaRepository<IdCard, Integer> {

//	@Query("select i from IdCard i where i.panNo=?1")
//	public IdCard getIdCardByPanno(String panNo);
	
	public IdCard findByPancard(PanCard pancard);
	
//	@Query("select i from IdCard i where i.adharNo=?1")
//	public IdCard getIdCardByAadharNo(Long adharNo);
	
//	public IdCard findByAdharCard( AdharCard adharcard );
	
	public List<IdCard> findByAdharcard( AdharCard adharcard );
	
}
