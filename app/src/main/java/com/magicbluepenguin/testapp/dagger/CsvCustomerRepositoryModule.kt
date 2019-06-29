package com.magicbluepenguin.testapp.dagger

import com.magicbluepenguin.testapp.customerrepository.CsvCustomerRepository
import com.magicbluepenguin.testapp.customerrepository.CustomerRepository
import dagger.Binds
import dagger.Module

@Module
abstract class CsvCustomerRepositoryModule {

    @Binds
    abstract fun bindCsvCustomerRepository(csvCustomerRepository: CsvCustomerRepository): CustomerRepository<String>
}