import React, { Component } from 'react'
import {
    Route,
    Switch,
    Redirect
} from 'react-router-dom'
import { injectIntl } from 'react-intl'

// Local
import Nav from '../UI/Nav'
import ErrorPage from '../UI/ErrorPage'

/**
 * Parent Component for our Application.
 */
class Main extends Component {

    render() {
        return (
            <div className='opentext-expctr has-bg-gradient ec-dark'>
                <Nav {...this.props}/>
                <div>                 
                    <Switch>
                        <Route path='/error/:errorType' component={ErrorPage} />
                        <Redirect to='/error/401' />
                    </Switch>
                </div>
              
            </div>
        )
    }
}

export default injectIntl(Main)
