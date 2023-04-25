// note that because this loads before the main root, we can't use getLogger()

//================================
// Localization Support
//================================
const defaultLocale = 'en'

// gets locale set by Browser, fails over to system default
export const getLocale = () =>  navigator.language || defaultLocale

