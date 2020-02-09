package gr.codehub.RecruMe.VEG.services;

import gr.codehub.RecruMe.VEG.dtos.ApplicantDto;
import gr.codehub.RecruMe.VEG.dtos.JobOfferDto;
import gr.codehub.RecruMe.VEG.exceptions.ApplicantNotFoundException;
import gr.codehub.RecruMe.VEG.exceptions.JobOfferNotFoundException;
import gr.codehub.RecruMe.VEG.models.*;
import gr.codehub.RecruMe.VEG.repositories.JobOffers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class JobOfferService {
    @Autowired
    private JobOffers jobOfferRepo;

    public JobOffer save(JobOfferDto jobOfferDto) {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setTitleOfPosition(jobOfferDto.getTitleOfPosition());
        jobOffer.setRegion(jobOfferDto.getRegion());
        jobOffer.setEducationLevel(jobOfferDto.getEducationLevel());
        // TO DO SET UP SKILLS

        List<JobSkill> jobSkills = jobOfferDto.getJobSkills();
        jobOffer.setActive(true);
        jobOffer = jobOfferRepo.save(jobOffer);;

        for (int i=0; i<jobSkills.size(); i++){
            jobSkills.get(i).setJobOffer(jobOffer);
        }
        return jobOfferRepo.save(jobOffer);
    }

    public List<JobOffer> getAll() {
        return
                StreamSupport
                        .stream(jobOfferRepo.findAll().spliterator(), false)
                        .collect(Collectors.toList());
    }
}

