HOMEPAGE = "https://github.com/openbmc/u-boot"
SECTION = "bootloaders"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
PE = "1"

# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "7fb88a88d08c19b0e6f88c0a484ab001a4476a27"

UBRANCH = "v2016.07-aspeed-openbmc"
SRC_URI = "git://git@github.com/openbmc/u-boot.git;branch=${UBRANCH};protocol=https"

S = "${WORKDIR}/git"

PV = "v2016.07+git${SRCPV}"
