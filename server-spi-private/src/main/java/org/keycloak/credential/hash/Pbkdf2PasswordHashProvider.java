/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.credential.hash;

import org.keycloak.Config;
import org.keycloak.common.util.Base64;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.UserCredentialModel;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * @author <a href="mailto:me@tsudot.com">Kunal Kerkar</a>
 */
public class Pbkdf2PasswordHashProvider extends APbkdf2PasswordHashProvider implements PasswordHashProviderFactory {

    public static final String ID = "pbkdf2";

    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int DERIVED_KEY_SIZE = 512;

    public CredentialModel encode(String rawPassword, int iterations) {
        byte[] salt = getSalt();
        String encodedPassword = encode(rawPassword, iterations, salt);

        CredentialModel credentials = new CredentialModel();
        credentials.setAlgorithm(ID);
        credentials.setType(UserCredentialModel.PASSWORD);
        credentials.setSalt(salt);
        credentials.setHashIterations(iterations);
        credentials.setValue(encodedPassword);
        return credentials;
    }

    @Override
    public PasswordHashProvider create(KeycloakSession session) {
        return this;
    }

    @Override
    public void init(Config.Scope config) {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public String getId() {
        return getAlgorithmAlias();
    }

    @Override
    protected String getAlgorithmAlias() {
        return ID;
    }

    @Override
    protected String getPbkdf2Algorithm() {
        return PBKDF2_ALGORITHM;
    }

    @Override
    protected int getDerivedKeySize() {
        return DERIVED_KEY_SIZE;
    }

}
