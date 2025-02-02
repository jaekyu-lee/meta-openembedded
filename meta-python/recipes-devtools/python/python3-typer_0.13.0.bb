SUMMARY = "Typer, build great CLIs. Easy to code. Based on Python type hints."
DESCRIPTION = "\
    Typer is a library for building CLI applications that users will love using and developers will love creating. Based on Python type hints. \
    It's also a command line tool to run scripts, automatically converting them to CLI applications. \
"
HOMEPAGE = "https://github.com/tiangolo/typer"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=173d405eb704b1499218013178722617"

SRC_URI[sha256sum] = "f1c7198347939361eec90139ffa0fd8b3df3a2259d5852a0f7400e476d95985c"

inherit pypi python_setuptools_build_meta ptest

DEPENDS += "\
    python3-pdm-backend-native \
    python3-pdm-native \
"

SRC_URI:append = " \
    file://run-ptest \
"

PYPI_PACKAGE = "typer"

RDEPENDS:${PN} += "\
    python3-click \
    python3-shellingham \
"

RDEPENDS:${PN}-ptest += "\
    python3-coverage \
    python3-pytest \
    python3-typing-extensions \
    python3-unittest-automake-output \
"

do_install_ptest() {
    install -d ${D}${PTEST_PATH}/tests
    cp -rf ${S}/tests/* ${D}${PTEST_PATH}/tests/
}

