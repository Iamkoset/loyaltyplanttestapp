package com.loyaltyplant.testapp.controller;

import com.loyaltyplant.testapp.controller.dto.AccountTransferDto;
import com.loyaltyplant.testapp.exceptions.NonexistentAccountException;
import com.loyaltyplant.testapp.exceptions.NotEnoughFundsException;
import com.loyaltyplant.testapp.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Resource(name = "transferService")
    private TransferService transferService;

    /**
     * Returns {@link ResponseEntity} which contain data of transfer operation
     *
     * Response status {@link HttpStatus#BAD_REQUEST} means that sender account
     * doesn't have enough funds
     *
     * Response status {@link HttpStatus#NOT_FOUND} means that at least one of
     * accounts wasn't found in data store
     *
     * @param dto {@link AccountTransferDto}
     * @return {@link ResponseEntity}
     * */
    @PutMapping
    public ResponseEntity<? super AccountTransferDto> transferFunds(@RequestBody AccountTransferDto dto) {
        AccountTransferDto result;
        try {
            result = transferService.transfer(dto.getFromAccount(),
                    dto.getToAccount(), dto.getAmount());

        } catch (NotEnoughFundsException nef) {
            return new ResponseEntity<>(nef.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NonexistentAccountException nae) {
            return new ResponseEntity<>(nae.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Returns {@link ResponseEntity} which contain data of withdraw operation
     *
     * Response status {@link HttpStatus#BAD_REQUEST} means that sender account
     * doesn't have enough funds
     *
     * @param dto {@link AccountTransferDto}
     * @return {@link ResponseEntity}
     * */
    @PutMapping("/withdraw")
    public ResponseEntity<? super AccountTransferDto> withdrawFunds(@RequestBody AccountTransferDto dto) {
        AccountTransferDto result;
        try {
            result = transferService.withdraw(dto.getFromAccount(), dto.getAmount());
        } catch (NotEnoughFundsException nef) {
            return new ResponseEntity<>(nef.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Returns {@link ResponseEntity} which contain data of add operation
     *
     * @param dto {@link AccountTransferDto}
     * @return {@link ResponseEntity}
     * */
    @PutMapping("/add")
    public ResponseEntity<AccountTransferDto> addFunds(@RequestBody AccountTransferDto dto) {
        AccountTransferDto result = transferService.add(dto.getToAccount(), dto.getAmount());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
