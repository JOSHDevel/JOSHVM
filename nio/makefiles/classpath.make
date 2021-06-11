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

MODULE_NAME=nio

# Javadoc source path
NIO_SOURCEPATH += $(BUILD_ROOT_DIR)/nio/src/share/classes

# Java files for the ( nio ) module
#

NIO_SHARE_JAVA_DIR = $(NIO_DIR)/src/share/classes

# Public API classes
#
MODULE_NIO_API_JAVA_FILES = \
    ${NIO_SHARE_JAVA_DIR}/java/lang/Comparable.java \
    ${NIO_SHARE_JAVA_DIR}/java/nio/Buffer.java \
    ${NIO_SHARE_JAVA_DIR}/java/nio/BufferOverflowException.java \
    ${NIO_SHARE_JAVA_DIR}/java/nio/BufferUnderflowException.java \
    ${NIO_SHARE_JAVA_DIR}/java/nio/ByteBuffer.java \
    ${NIO_SHARE_JAVA_DIR}/java/nio/FloatBuffer.java \
    ${NIO_SHARE_JAVA_DIR}/java/nio/IntBuffer.java \
    ${NIO_SHARE_JAVA_DIR}/java/nio/ShortBuffer.java \
    ${NIO_SHARE_JAVA_DIR}/java/nio/ByteOrder.java \
    ${NIO_SHARE_JAVA_DIR}/java/nio/LongBuffer.java

ifneq ($(USE_JSR_177), true)
# IMPL_NOTE: temporary fix: remove this class as it is also defined by JSR 177
MODULE_NIO_API_JAVA_FILES += \
    ${NIO_SHARE_JAVA_DIR}/java/lang/UnsupportedOperationException.java
endif

NIO_CLDC_IMPL_JAVA_DIR = $(NIO_DIR)/src/cldc/classes

# NIO Impl classes
#
MODULE_NIO_IMPL_JAVA_FILES += \
    ${NIO_CLDC_IMPL_JAVA_DIR}/java/nio/ByteBufferImpl.java \
    ${NIO_CLDC_IMPL_JAVA_DIR}/java/nio/FloatBufferImpl.java \
    ${NIO_CLDC_IMPL_JAVA_DIR}/java/nio/IntBufferImpl.java \
    ${NIO_CLDC_IMPL_JAVA_DIR}/java/nio/ShortBufferImpl.java \
    ${NIO_CLDC_IMPL_JAVA_DIR}/java/nio/LongBufferImpl.java

# GL/EGL Impl classes
#
MODULE_NIO_COM_SUN_JAVA_FILES += \
    ${NIO_CLDC_IMPL_JAVA_DIR}/com/sun/jsr239/BufferManager.java

# Determines what option we have made and assigns it to the
# variable that the global makefile recognizes.
#
JSR_JAVA_FILES_DIR += \
    $(MODULE_NIO_API_JAVA_FILES) \
    $(MODULE_NIO_IMPL_JAVA_FILES) \
    $(MODULE_NIO_COM_SUN_JAVA_FILES)

DOC_SOURCE_$(MODULE_NAME)_PATH=$(NIO_SOURCEPATH)
DOC_SOURCE_PATH := $(DOC_SOURCE_PATH)$(DOC_SOURCE_$(MODULE_NAME)_PATH)$(DOC_PATH_SEP)
DOC_$(MODULE_NAME)_PACKAGES += java.nio