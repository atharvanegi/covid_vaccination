package com.coviwin.implementation;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.coviwin.exception.MemberException;
import com.coviwin.exception.VaccinationCenterException;
import com.coviwin.model.Appointment;
import com.coviwin.model.VaccinationCenter;
import com.coviwin.model.VaccineCount;
import com.coviwin.model.VaccineInventory;
import com.coviwin.repo.VaccinationCenterRepo;
import com.coviwin.service.VaccinationCenterService;


@Service
public class VaccinationCenterServiceImpl  implements VaccinationCenterService{

	@Autowired
	VaccinationCenterRepo vcrepo;

	@Override
	public List<VaccinationCenter> getAllVaccineCenters() throws VaccinationCenterException {
		List<VaccinationCenter> vcall =vcrepo.findAll();
		if(vcall.size()==0)
			throw new VaccinationCenterException("There are no centers available");
		
		return vcall;
	}

	@Override
	public VaccinationCenter getVaccineCenters(Integer centerid) throws VaccinationCenterException {
	java.util.Optional<VaccinationCenter> op = vcrepo.findById(centerid);
	if(op.isPresent())
		return op.get();
	
	throw new VaccinationCenterException("No center found");
	}

	@Override
	public VaccinationCenter addVaccineCenter(VaccinationCenter center) throws VaccinationCenterException {		
	
		if(center.getCode() != null) {
        Optional<VaccinationCenter> opt = vcrepo.findById(center.getCode());

        if(opt.isPresent()) {
            throw new VaccinationCenterException("VaccinationCenterException is already registered with VaccinationCenterExceptionId : " + center.getCode());
        }
		}
		
    List<Appointment> appList = center.getAppointments();
		
    if(appList != null) {
        for(Appointment app : appList) {

          app.setVaccinationCenter(center); // associating each appointment with VaccinationCenter 
        }
		
    }else {
        	
        	appList = new ArrayList<>();
        	
    }
        
        
		
	
		VaccineInventory vaccineInventory = center.getVaccineInventory();
		
		if(vaccineInventory != null) 
			vaccineInventory.getVaccinationCenters().add(center); // associating VaccineInventory with VaccinationCenter 
		
		return vcrepo.save(center);
		

	}

	@Override
	public VaccinationCenter updateVaccineCenter(VaccinationCenter center) throws VaccinationCenterException {

		

		Optional<VaccinationCenter> op = vcrepo.findById(center.getCode());
		
	    if(op.isPresent())
	    {
	    	
	    	List<Appointment> appList = center.getAppointments();
			
			for(Appointment app : appList) {
				
				app.setVaccinationCenter(center); // associating each appointment with VaccinationCenter 
			}
			
			center.getVaccineInventory().getVaccinationCenters().add(center); // associating VaccineInventory with VaccinationCenter 
	    	
	    	return vcrepo.save(center);

	    }	
	    
	    throw new VaccinationCenterException("No center found to update");
	}

	@Override
	public VaccinationCenter deleteVaccineCenter(VaccinationCenter center) throws VaccinationCenterException {
		Optional<VaccinationCenter> op = vcrepo.findById(center.getCode());
	    if(op.isPresent())
	    {
	    	vcrepo.deleteById(center.getCode());
	    	return op.get();
	    }
	    
		throw new VaccinationCenterException("No Center found to update");
	}
	
	
	
	

}
