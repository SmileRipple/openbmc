SUMMARY = "Phosphor OpenBMC MBOX Daemon"
DESCRIPTION = "Phosphor OpenBMC MBOX Daemon"
PR = "r1"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e"

inherit autotools pkgconfig
inherit obmc-phosphor-systemd

DEPENDS += "autoconf-archive-native"
DEPENDS += "systemd"
DEPENDS += "phosphor-logging"
RDEPENDS_${PN} += "libsystemd"

S = "${WORKDIR}/git"

SRC_URI += "git://github.com/openbmc/phosphor-mboxd.git"

SRC_URI += "file://99-aspeed-mbox.rules"
SRC_URI += "file://99-aspeed-lpc-ctrl.rules"
SRC_URI += "file://aspeed-lpc-ctrl-h.patch"

SRCREV="cafb002ebcbe8c9a828a1b3c15d2f9b95fb7e9ec"

PROVIDES += "mboxctl"

MBOXD_FLASH_SIZE ??= "32M"
SYSTEMD_SUBSTITUTIONS += "FLASH_SIZE:${MBOXD_FLASH_SIZE}:${PN}.service"

# Hacks because ${STAGING_KERNEL_DIR} points to the kernel source tree, not the
# installed, pre-processed headers. Requires the aspeed-lpc-ctrl-h patch above.
CFLAGS_append = " -I include"

do_install_append() {
    install -d ${D}/lib/udev/rules.d
    install -m 0644 ${WORKDIR}/99-aspeed-mbox.rules ${D}/lib/udev/rules.d
    install -m 0644 ${WORKDIR}/99-aspeed-lpc-ctrl.rules ${D}/lib/udev/rules.d
}

TMPL = "mboxd-reload@.service"
TGTFMT = "obmc-chassis-poweron@{0}.target"
INSTFMT = "mboxd-reload@{0}.service"
FMT = "../${TMPL}:${TGTFMT}.wants/${INSTFMT}"

SYSTEMD_SERVICE_${PN} += "mboxd.service"
SYSTEMD_SERVICE_${PN} += "mboxd-reload@.service"
SYSTEMD_LINK_${PN} += "${@compose_list(d, 'FMT', 'OBMC_CHASSIS_INSTANCES')}"

# Enable virtual-pnor by MACHINE_FEATURE openpower-ubi-fs.
PACKAGECONFIG_append = "${@mf_enabled(d, 'openpower-ubi-fs', 'virtual-pnor')}"
PACKAGECONFIG[virtual-pnor] = "--enable-virtual-pnor,--disable-virtual-pnor"
