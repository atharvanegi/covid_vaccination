package com.coviwin.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coviwin.exception.VaccineException;
import com.coviwin.exception.VaccineInventoryException;
import com.coviwin.model.VaccinationCenter;
import com.coviwin.model.Vaccine;
import com.coviwin.model.VaccineCount;
import com.coviwin.model.VaccineInventory;
import com.coviwin.repo.VaccinationCenterRepo;
import com.coviwin.repo.VaccineInventoryRepo;
import com.coviwin.repo.VaccineRepo;
import com.coviwin.service.VaccineInventoryService;

@Service
public class VaccineInventoryServiceImpl implements VaccineInventoryService {

	@Autowired
	private VaccineInventoryRepo vacInvRepo;
	
	@Autowired
	private VaccinationCenterRepo vacCenterRepo;
	
	@Autowired
	private VaccineRepo vacRepo;
	
	@Override
	public List<VaccineInventory> allVaccineInventory()throws VaccineInventoryException {
		
		List<VaccineInventory> list = vacInvRepo.findAll();
		
		if(list.isEmpty()) {
			throw new VaccineInventoryException("No VaccineInventory Found..");
		}else
			return list;
	}

	
	@Override
	public VaccineInventory addVaccineCount(Integer vacid, Integer vacInv, VaccineCount vacineCount)throws VaccineInventoryException, VaccineException {

		
	VaccineInventory vaccineInventory = vacInvRepo.findById(vacInv)
				                                   .orElseThrow(() -> new VaccineInventoryException("No VaccineInventory found with details : "+ vacInv));
	
	Vaccine vaccine = vacRepo.findById(vacid)
							 .orElseThrow( ()->new VaccineException( "No vaccine found with that id" ) );
	
	vacineCount.setVaccineInventory( vaccineInventory ); // associating vacineCount with VaccineInventory
	vacineCount.setVaccine(vaccine);
	vaccine.setVaccinecount(vacineCount);
	vaccineInventory.getVaccineCounts().add(vacineCount);
	
	return vacInvRepo.save(vaccineInventory);
	
	}

	
	@Override
	public VaccineInventory addVaccineInventoryByCenter(Integer centerId, VaccineInventory vInventory)
			throws VaccineInventoryException {
		

		
		VaccinationCenter vaccinationCenter = vacCenterRepo.findById(centerId)
                .orElseThrow(() -> new VaccineInventoryException("No VaccinationCenter found with centerId : "+ centerId)) ;
		
//		if(vInventory.getVaccineInventoryId() != null) { // if auto-generated ID value (as 1st JPA comes than MySql, by which if it is not passed than by default it get's null value
	
		
	    vaccinationCenter.setVaccineInventory( vInventory ); // associating vaccinationCenter with VaccineInventory
 
	    List<VaccineCount> VacCountList = vInventory.getVaccineCounts();
	    
	    for(VaccineCount vacCount : VacCountList) {
	    	
	    	vacCount.setVaccineInventory(vInventory); // associating each VaccineCount with VaccineInventory
	    }
	    
	    
	    if( vInventory.getVaccinationCenters() != null)
		    vInventory.getVaccinationCenters().add(vaccinationCenter);  // adding the passed centerId vaccination center
	    
		else {
		    	vInventory.setVaccinationCenters(new ArrayList<VaccinationCenter>());
		    	vInventory.getVaccinationCenters().add(vaccinationCenter);
		    }
	    
	    return vacInvRepo.save(vInventory);
	    
	    
//		}
		
//		else {
//			
//		    vaccinationCenter.setVaccineInventory( vInventory ); // associating vaccinationCenter with VaccineInventory
//		    
//		    List<VaccineCount> VacCountList = vInventory.getVaccineCounts();
//		    
//		    for(VaccineCount vacCount : VacCountList) {
//		    	
//		    	vacCount.setVaccineInventory(vInventory); // associating each VaccineCount with VaccineInventory
//		    }
//		    
//		    
//		    if( vInventory.getVaccinationCenters() != null)
//		    vInventory.getVaccinationCenters().add(vaccinationCenter);
//		    else {
//		    	vInventory.setVaccinationCenters(new ArrayList<VaccinationCenter>());
//		    	vInventory.getVaccinationCenters().add(vaccinationCenter);
//		    }
//		    
//		    return vacInvRepo.save(vInventory);
//		}
			
	
	}

	@Override
	public VaccineInventory updateVaccineInventory(Integer vaccineInventory, VaccineInventory vInventory)
			throws VaccineInventoryException {

          vacInvRepo.findById(vaccineInventory).orElseThrow(() -> new VaccineInventoryException("No VaccineInventory found with Id : "+vaccineInventory));
          
          
          List<VaccinationCenter> centerList = vInventory.getVaccinationCenters();
          
          for(VaccinationCenter vacCenter : centerList) {
        	  
        	  vacCenter.setVaccineInventory(vInventory); // associating each VaccinationCenter with updated VaccineInventory
          }
          
          
          List<VaccineCount> VacCountList = vInventory.getVaccineCounts();
  	    
  	    for(VaccineCount vacCount : VacCountList) {
  	    	
  	    	vacCount.setVaccineInventory(vInventory); // associating each VaccineCount with updated VaccineInventory
  	    }
  	    
          
          return vacInvRepo.save(vInventory);
          
	}

	@Override
	public Boolean deleteVaccineInventory(Integer vaccineInventory) throws VaccineInventoryException {

        vacInvRepo.findById(vaccineInventory).orElseThrow(() -> new VaccineInventoryException("No VaccineInventory found with Id : "+vaccineInventory));

        vacInvRepo.deleteById(vaccineInventory);
        
        return true;
	}

	@Override
	public List<VaccineInventory> getVaccineInventoryByDate(LocalDate date) throws VaccineInventoryException {
		
		List<VaccineInventory> list = vacInvRepo.findBydate(date);
		
		if(list.isEmpty()) {
			throw new VaccineInventoryException("No VaccineInventory found with data : " + date);
		}else
			return list;
		
	}

	@Override
	public List<VaccineInventory> getVaccineInventoryByVaccine(Vaccine vaccine) throws VaccineInventoryException {
		
		List<VaccineInventory> result = new ArrayList<>();
		
		List<VaccineInventory> list = vacInvRepo.findAll();
		
		if(list.isEmpty()) {
			throw new VaccineInventoryException("No VaccineInventory found, first add some Inventory");
			
		}
			
			for(VaccineInventory vacInv : list) {
				
			  List<VaccineCount> vacCountList =	vacInv.getVaccineCounts();
			  
			  if(vacCountList.isEmpty()) {
				   throw new VaccineInventoryException("No VaccineCount found, first add some VaccineCount");
			  
			  }
				  
				  for(VaccineCount vacCount : vacCountList) {
					  
					  if(vacCount.getVaccine() == vaccine) {
						  result.add(vacInv);
					  }
					  
				  }
				
			}
			
		
		
		if(result.isEmpty()) {
			   throw new VaccineInventoryException("No VaccineInventory found with Vaccine : " + vaccine);

		}else
			return result;
		
	}

}
