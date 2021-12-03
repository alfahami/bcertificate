/*
SPDX-License-Identifier: Apache-2.0
*/

package org.example;

import org.junit.Test;

public class ClientTest {

	@Test
	public void testCertificate() throws Exception {
		EnrollAdmin.main(null);
		RegisterUser.main(null);
		ClientApp.main(null);
	}
}
