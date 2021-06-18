# Copyright (C) Max Mu
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
# 
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# version 2, as published by the Free Software Foundation.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License version 2 for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
# 
# Please visit www.joshvm.org if you need additional information or
# have any questions.

MODULE_NAME=josh-crypto

include $(SECURITY_DIR)/makefiles/module.config

ifeq ($(ENABLE_SECURITY), true)

JSR_JAVA_FILES_DIR += $(SECURITY_DIR)/pki/interface

DOC_SOURCE_SECURITY_PATH := $(BUILD_ROOT_DIR)/security/pki/interface
DOC_$(MODULE_NAME)_PACKAGES += javax.microedition.pki 

ifeq ($(USE_BOUNCYCASTLE), true)
JSR_JAVA_FILES_DIR += $(SECURITY_DIR)/publickeystore/reference/classes
JSR_JAVA_FILES_DIR += $(SECURITY_DIR)/pki/reference/classes
JSR_JAVA_FILES_DIR += $(SECURITY_DIR)/keystore/classes
JSR_JAVA_FILES_DIR += $(SECURITY_DIR)/ssl/bcssl/classes
JSR_JAVA_FILES_DIR += $(SECURITY_DIR)/crypto/reference/classes
else
JSR_JAVA_FILES_DIR += $(SECURITY_DIR)/ssl/nativessl/classes
endif

ifeq ($(ENABLE_SECURITY_NATIVE_RSA_SIGNATURE), true)
JSR_JAVA_FILES_DIR += $(SECURITY_DIR)/signatures/classes
DOC_$(MODULE_NAME)_PACKAGES += com.sun.midp.crypto
endif

ifeq ($(USE_BOUNCYCASTLE), true)
JSR_JAVA_FILES_DIR += $(SECURITY_DIR)/bouncycastle/classes

DOC_SOURCE_SECURITY_BOUNCYCASTLE_PATH := $(BUILD_ROOT_DIR)/security/bouncycastle/classes
DOC_$(MODULE_NAME)_PACKAGES += org.bouncycastle.crypto org.bouncycastle.crypto.digests org.bouncycastle.crypto.encodings \
							org.bouncycastle.crypto.engines org.bouncycastle.crypto.generators org.bouncycastle.crypto.prng  \
							org.bouncycastle.crypto.paddings org.bouncycastle.crypto.macs org.bouncycastle.crypto.modes  \
							org.bouncycastle.crypto.signers org.bouncycastle.crypto.util org.bouncycastle.util org.bouncycastle.util.encoders \
							org.bouncycastle.crypto.tls org.bouncycastle.crypto.io org.bouncycastle.crypto.params 

DOC_SOURCE_SECURITY_KEYSTORE_PATH := $(BUILD_ROOT_DIR)/security/keystore/classes
DOC_$(MODULE_NAME)_PACKAGES += org.joshvm.crypto.keystore 
                            
DOC_SOURCE_PATH := $(DOC_SOURCE_PATH)$(DOC_SOURCE_SECURITY_BOUNCYCASTLE_PATH)$(DOC_PATH_SEP)$(DOC_SOURCE_SECURITY_PATH)$(DOC_PATH_SEP)
DOC_SOURCE_PATH := $(DOC_SOURCE_PATH)$(DOC_SOURCE_SECURITY_KEYSTORE_PATH)$(DOC_PATH_SEP)
else
DOC_SOURCE_PATH := $(DOC_SOURCE_PATH)$(DOC_SOURCE_SECURITY_PATH)$(DOC_PATH_SEP)
endif

endif