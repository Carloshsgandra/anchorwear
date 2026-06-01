package com.javaduolingo.repository;

import com.javaduolingo.model.Address;
import com.javaduolingo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
    Optional<Address> findByUserAndDefaultAddressTrue(User user);
}
