/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.orchestrator.plugin.huaweicloud.models;

import java.util.HashMap;

/**
 * Huawei cloud publicIp property.
 */
public class HuaweiPublicIpProperty extends HashMap<String, String> {

    /**
     * Init method to put property key and value.
     */
    public HuaweiPublicIpProperty() {
        this.put("ip", "address");
    }
}