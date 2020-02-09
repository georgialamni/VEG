package gr.codehub.RecruMe.VEG.services;

import gr.codehub.RecruMe.VEG.exceptions.ApplicantNotFoundException;
import gr.codehub.RecruMe.VEG.models.Applicant;
import gr.codehub.RecruMe.VEG.repositories.Applicants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ApplicantSearchService {
    @Autowired
    private Applicants applicantRepo;

    public Applicant getApplicant(int id) throws ApplicantNotFoundException {

        try {
            Applicant applicant = applicantRepo.findById(id).get();
            return applicant;
        } catch (Exception e) {
            throw new ApplicantNotFoundException("Applicant id = " + id + " NOT FOUND");
        }

    }

    public List<Applicant> searchByName(String firstName) throws ApplicantNotFoundException {
        try {
            return
                    StreamSupport
                            .stream(applicantRepo.findAll().spliterator(), false)
                            .filter(applicant -> applicant.getFirstName().equalsIgnoreCase(firstName))
                            .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ApplicantNotFoundException(firstName + " NOT FOUND");
        }
    }

    public List<Applicant> searchByRegion(String region) throws ApplicantNotFoundException {
        try {
            return
                    StreamSupport
                            .stream(applicantRepo.findAll().spliterator(), false)
                            .filter(applicant -> applicant.getRegion().equalsIgnoreCase(region))
                            .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ApplicantNotFoundException("Region = " + region + " NOT FOUND");
        }
    }
}
