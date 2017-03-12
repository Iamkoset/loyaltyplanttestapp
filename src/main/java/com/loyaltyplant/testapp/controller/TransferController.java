package com.loyaltyplant.testapp.controller;

import com.loyaltyplant.testapp.controller.dto.AccountTransferDto;
import com.loyaltyplant.testapp.exceptions.AccountDoesntExistException;
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

    @PutMapping
    public ResponseEntity<? super AccountTransferDto> transferFunds(@RequestBody AccountTransferDto dto) {
        AccountTransferDto result;
        try {
            result = transferService.transfer(dto.getFromAccount(),
                    dto.getToAccount(), dto.getAmount());

        } catch (NotEnoughFundsException nef) {
            return new ResponseEntity<>(nef.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (AccountDoesntExistException ade) {
            return new ResponseEntity<>(ade.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

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

    @PutMapping("/add")
    public ResponseEntity<AccountTransferDto> addFunds(@RequestBody AccountTransferDto dto) {
        AccountTransferDto result = transferService.add(dto.getToAccount(), dto.getAmount());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
