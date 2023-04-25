// ************************************************************************************************************
//
// Copyright (c) 2021 Open Text. All Rights Reserved.
// Trademarks owned by Open Text.
//
// No part of this document may be photocopied, reproduced, translated, or transmitted in any form
// or by any means without the prior written consent of OpenText. Any unauthorized duplication
// in whole or in part is strictly prohibited by United States federal law. OpenText will enforce
// its copyright to the full extent of the law.
//
// ************************************************************************************************************

//================================
// Application Imports
//================================
import React from 'react'
import ReactDOM from 'react-dom'

import { AppContainer } from 'react-hot-loader'

import App from 'AppApps/App'
import { getMessages } from './envloader'

// Thirdparty CSS
import 'bootstrap/dist/css/bootstrap.css'
import { getLocale } from './localebuilder'

import '../app/css/sass/main.scss'

const locale = getLocale()
const messages = getMessages()

//===================================
// Main React Container: Root
//===================================
const Root = () => {    
    return (
        <App locale={locale} messages={messages}/>
    )}
//===================================
// Render Root component into HTML
//===================================
ReactDOM.render(
    <Root />,
    document.getElementById('root')
)

// Hot Module Replacement API for development only
if (module.hot) {
    module.hot.accept(Root, () => {
        const NextApp = Root
        ReactDOM.render(
            <AppContainer>
                <NextApp />
            </AppContainer>,
            document.getElementById('root')
        )
    })
}