package org.eclipse.osc.orchestrator.plugin.openstack;

import org.eclipse.osc.modules.ocl.loader.Ocl;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.Network;

import java.util.Optional;

public class NeutronManager {

    public static void createNetwork(Ocl ocl, OSClient.OSClientV3 os) throws Exception {
        ocl.getNetwork().getSubnet().forEach(subnet -> {
            os.networking().subnet().create(Builders.subnet()
                    .name(subnet.getId())
                    .ipVersion(IPVersionType.V4)
                    .cidr(subnet.getCidr())
                    .build());
        });
    }

    public static String getVmNetworkId(OSClient.OSClientV3 osClient, String vmNetworkName) {
        Optional<? extends Network> network;
        network = osClient.networking()
                .network()
                .list()
                .stream()
                .filter(networkInfo -> networkInfo.getName().equalsIgnoreCase(vmNetworkName))
                .findAny();
        if (network.isEmpty()) {
            throw new RuntimeException("No network with name " + network + " found");
        }
        return network.get().getId();
    }

}