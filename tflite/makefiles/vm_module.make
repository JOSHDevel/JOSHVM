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
NIO_SRC_DIR = $(NIO_DIR)/src

JSR_EXT_INCLUDE_DIRS += \
  -I"$(NIO_SRC_DIR)/cldc/native/inc"
  
ROMGEN_CFG_FILES += ${NIO_SRC_DIR}/cldc-oi/config/nio_rom.cfg 


ifeq ($(compiler), visCPP)
obj_ext = obj
build_c = $(BUILD_C_TARGET_NO_PCH)
else
obj_ext = o
build_c = $(BUILD_C_TARGET)
endif

NIO_Obj_Files = \
	JSR239-KNIBuffer.$(obj_ext) \
	JSR239-KNIMemory.$(obj_ext)
	
JSR239-KNIBuffer.$(obj_ext): $(NIO_DIR)/src/cldc/native/JSR239-KNIBuffer.c
	$(build_c)

JSR239-KNIMemory.$(obj_ext): $(NIO_DIR)/src/cldc-oi/common/JSR239-KNIMemory.c
	$(build_c)
