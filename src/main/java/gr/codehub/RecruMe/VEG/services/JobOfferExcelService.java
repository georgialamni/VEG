package gr.codehub.RecruMe.VEG.services;

import gr.codehub.RecruMe.VEG.models.JobOffer;
import gr.codehub.RecruMe.VEG.models.JobSkill;
import gr.codehub.RecruMe.VEG.models.Skill;
import gr.codehub.RecruMe.VEG.repositories.JobOffers;
import gr.codehub.RecruMe.VEG.repositories.JobSkills;
import gr.codehub.RecruMe.VEG.repositories.Skills;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class JobOfferExcelService {
    @Autowired
    private JobOffers jobOfferRepo;
    @Autowired
    private Skills skillRepo;
    @Autowired
    private JobSkills jobSkillRepo;


    public List<JobOffer> getJobOfferExcel() throws IOException {
        JobOfferExcel("dataforrecrume.xlsx");
        return jobOfferRepo.findAll();
    }

    public void JobOfferExcel(String excelFileName) throws IOException {

        File file = ResourceUtils.getFile("classpath:"+excelFileName);
        FileInputStream excelFile = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(1);
        Iterator<Row> row = datatypeSheet.iterator();

        row.next();

        while (row.hasNext()) {
            Row currentRow = row.next();
            Iterator<Cell> cellIterator = currentRow.iterator();

            Cell companyCell = cellIterator.next();
            Cell titleOfPositionCell = cellIterator.next();
            Cell regionCell = cellIterator.next();
            Cell educationLevelCell = cellIterator.next();

            List<Skill> jobSkills = new ArrayList<>();


            String description = "";
            while(cellIterator.hasNext()) {
                Cell descr =  cellIterator.next();
                String value = descr.getStringCellValue();
//                if (!value.trim().isEmpty()){
//                    if (!description.isEmpty()){
//                        description += ", ";
//                    }
//                    description += value;
                Skill foundSkill = skillRepo.findFirstByDescription(value);
                jobSkills.add(foundSkill);
            }

            JobOffer jobOffer = new JobOffer(
                    companyCell.getStringCellValue(),
                    titleOfPositionCell.getStringCellValue(),
                    regionCell.getStringCellValue(),
                    educationLevelCell.getStringCellValue());
            jobOffer = jobOfferRepo.save(jobOffer);

            for (int i=0; i<jobSkills.size(); i++){
                JobSkill jobSkill = new JobSkill();
                jobSkill.setJobOffer(jobOffer);
                jobSkill.setSkill(jobSkills.get(i));
                jobSkillRepo.save(jobSkill);
            }
            jobOffer.setActive(true);
            jobOfferRepo.save(jobOffer);
        }
    }
}