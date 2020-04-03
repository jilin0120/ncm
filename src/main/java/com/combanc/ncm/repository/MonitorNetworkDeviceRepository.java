package com.combanc.ncm.repository;

import com.combanc.ncm.entity.MonitorNetworkDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonitorNetworkDeviceRepository extends JpaRepository<MonitorNetworkDevice, Long> {

    public List<MonitorNetworkDevice> findMonitorNetworkDevicesByIsBakEquals(Integer isBak);
}
