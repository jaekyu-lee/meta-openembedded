SUMMARY = "GNOME Shell is the graphical shell of the GNOME desktop environment"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"


DEPENDS = " \
    libxml2-native \
    gtk4 \
    mutter \
    evolution-data-server \
    gcr \
    geocode-glib \
    gjs \
    gnome-autoar \
    gnome-desktop \
    gnome-control-center \
    polkit \
    pipewire \
    libsoup-3.0 \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '', 'startup-notification', d)} \
    ibus \
    gsettings-desktop-schemas \
"

inherit gnomebase gsettings gettext gobject-introspection gtk-icon-cache features_check bash-completion

REQUIRED_DISTRO_FEATURES = "x11 polkit systemd pam"

GTKIC_VERSION = "4"
GTKDOC_MESON_OPTION = "gtk_doc"
GIR_MESON_OPTION = ""

# gobject-introspection is mandatory and cannot be configured
REQUIRED_DISTRO_FEATURES += "gobject-introspection-data"

SRC_URI[archive.sha256sum] = "b467575f8c159d20557258517f0dd68dfd0147d11b48d97ab8e1f709bc522a63"
SRC_URI += "file://0001-Introduce-options-gjs_path-to-optionally-set-path-to.patch"

PACKAGECONFIG ??= "bluetooth nm ${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)}"
PACKAGECONFIG[bluetooth] = ",,gnome-bluetooth"
PACKAGECONFIG[nm] = "-Dnetworkmanager=true, -Dnetworkmanager=false,networkmanager libsecret,networkmanager"
PACKAGECONFIG[systemd] = "-Dsystemd=true, -Dsystemd=false, systemd"

EXTRA_OEMESON = " \
    -Dgjs_path=${bindir}/gjs \
    -Dextensions-app:gjs_path=${bindir}/gjs \
    -Dtests=false \
    -Dman=false \
"

do_install:append() {
    # fix shebangs
    for tool in `find ${D}${bindir} -name '*-tool'`; do
        sed -i 's:#!${PYTHON}:#!${bindir}/python3:' $tool
    done
}

GSETTINGS_PACKAGE = "${PN}-gsettings"

FILES:${PN} += " \
    ${datadir}/metainfo \
    ${datadir}/dbus-1 \
    ${datadir}/gnome-control-center \
    ${datadir}/xdg-desktop-portal \
    ${systemd_user_unitdir} \
"

RDEPENDS:${PN} += " \
	accountsservice \
	adwaita-icon-theme \
	adwaita-icon-theme-cursors \
	gdm-base \
	gnome-control-center \
	gnome-backgrounds \
	gnome-bluetooth \
	gnome-desktop \
	gnome-session \
	gnome-settings-daemon \
	gnome-shell-gsettings \
	gsettings-desktop-schemas \
	librsvg-gtk \
"

PACKAGES =+ "${PN}-tools ${PN}-gsettings"
FILES:${PN}-tools = "${bindir}/*-tool"
RDEPENDS:${PN}-tools = "python3-core"

