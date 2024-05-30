package com.github.mwacha.infra.analysis.job;

import lombok.NoArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.mwacha.domain.analysis.Charge;
import com.github.mwacha.infra.analysis.repository.ChargeRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Component
@NoArgsConstructor
public class AnalysisChargeItemReader implements ItemReader<List<Charge>> {

    private List<Long> clientIds;
    private Iterator<Long> clientIdsIterator;


    @Autowired
    private ChargeRepository repository;

    @Override
    public List<Charge> read() {
        if (clientIdsIterator == null || !clientIdsIterator.hasNext()) {
            return null;
        }

        List<Charge> charges = new ArrayList<>();
        while (clientIdsIterator.hasNext()) {
            Long clientId = clientIdsIterator.next();
            repository.findByClientId(clientId).ifPresent(charges::add);
        }

        return charges;
    }


    public void setClientIds(List<Long> clientIds) {
        this.clientIds = clientIds;
        this.clientIdsIterator = this.clientIds.iterator();
    }

}
