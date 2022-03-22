package com.bcad.application.service;

import com.bcad.application.domain.BCADPMSNav;
import com.bcad.application.domain.ProfitShare;
import com.bcad.application.repository.ProfitShareRepository;
import com.bcad.application.service.dto.ClientInvestmentDateDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InvestmentDateFile {

    private final ProfitShareRepository profitShareRepository;
    private final PMSUploadService pmsUploadService;

    public InvestmentDateFile(ProfitShareRepository profitShareRepository,PMSUploadService pmsUploadService) {
        this.profitShareRepository = profitShareRepository;
        this.pmsUploadService = pmsUploadService;
    }

    public int getBCADClients() {
        List<ProfitShare> result = profitShareRepository.getInvestment();
        int counter = 0;
        List<ClientInvestmentDateDTO> clientList = new ArrayList();
        try {
            clientList = pmsUploadService.readExcelFile();
            for (ProfitShare profitShare : result) {
                ClientInvestmentDateDTO clientInvestmentDateDTO = clientList.stream().filter(x -> (x.getClientCode().equals(profitShare.getPmsClientMaster().getClientCode())
                 && (x.getSchemeName()).equals(profitShare.getInvestmentMaster().getInvestmentName())))
                    .findAny().orElse(null);
                if(clientInvestmentDateDTO!=null){
                    profitShare.setInvestmentDate(clientInvestmentDateDTO.getInvestmentDate());
                    profitShareRepository.save(profitShare);
                }


            }
            System.out.println("Total records matched... " + counter);
            System.out.println("File rows count... " + clientList.size());
        } catch (Exception e) {
            System.out.println("File Not Found...." + e.getMessage());
        }

        return counter;
    }
}
