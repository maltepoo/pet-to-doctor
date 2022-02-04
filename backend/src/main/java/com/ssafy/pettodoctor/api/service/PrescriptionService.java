package com.ssafy.pettodoctor.api.service;

import com.ssafy.pettodoctor.api.domain.Prescription;
import com.ssafy.pettodoctor.api.domain.Treatment;
import com.ssafy.pettodoctor.api.domain.TreatmentType;
import com.ssafy.pettodoctor.api.repository.PrescriptionRepository;
import com.ssafy.pettodoctor.api.repository.TreatmentRepositry;
import com.ssafy.pettodoctor.api.request.PrescriptionPostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final TreatmentRepositry treatmentRepositry;

    public List<Prescription> findByIdList(Long doctor_id, TreatmentType type) {
        return prescriptionRepository.findByIdList(doctor_id, type);
    }

    @Transactional
    public void writeCertificate(PrescriptionPostReq certificateInfo, Long treatmentId){
        Prescription prescription = Prescription.createPrescription(
                certificateInfo.getAdministration(),
                certificateInfo.getDiagnosis(),
                certificateInfo.getMedicine(),
                certificateInfo.getOpinion(),
                certificateInfo.getPrice(),
                certificateInfo.getType()
        );

        prescriptionRepository.save(prescription);
        Treatment treatment = treatmentRepositry.findByTreatmentId(treatmentId);
        treatment.setPrescription(prescription);
    }

    @Transactional
    public Optional<Prescription> updateCertificate(Long prescription_id, PrescriptionPostReq certificateInfo){
        Optional<Prescription> updatePrescription= Optional.ofNullable(prescriptionRepository.findById(prescription_id));

        updatePrescription.ifPresent(selectPrescription -> {
            selectPrescription.setIsShipping(certificateInfo.getIsShipping());
            selectPrescription.setInvoiceCode(certificateInfo.getInvoiceCode());
            selectPrescription.setPaymentCode(certificateInfo.getPaymentCode());
            selectPrescription.setShippingAddress(certificateInfo.getAddress());
            selectPrescription.setShippingName(certificateInfo.getShippingName());
            selectPrescription.setShippingTel(certificateInfo.getShippingTel());

            prescriptionRepository.save(selectPrescription);
        });
        return updatePrescription;
    }



    public Prescription findById(Long id) {return prescriptionRepository.findById(id); }
}