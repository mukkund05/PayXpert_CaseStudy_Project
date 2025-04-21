package com.hexaware.dao;

import com.hexaware.entity.Tax;
import com.hexaware.exception.TaxCalculationException;

import java.util.List;

public interface ITaxService {
    double calculateTax(int employeeID, int taxYear) throws TaxCalculationException;
    Tax getTaxById(int taxID) throws TaxCalculationException;
    List<Tax> getTaxesForEmployee(int employeeID) throws TaxCalculationException;
    List<Tax> getTaxesForYear(int taxYear) throws TaxCalculationException;
}