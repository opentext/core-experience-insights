import React, {useState} from 'react'
import { Route, Switch, Redirect } from 'react-router-dom'
import { injectIntl } from 'react-intl'
import DatasetReport from './Reports/DatasetReport'
import { Alert } from 'reactstrap'

/**
 * Parent Component for our Application.
 */
function Main(props) {
    const { ...others } = props

    
    const [errorMessage, setErrorMessage] = useState('')
    const handleErrorMessage = (message) => setErrorMessage(message)
    return (
        <div id='opentext-sample-ui-main' className='w-100' role='main'>
            <div>
                {errorMessage && 
                    <Alert className='message m-auto' color='danger'>
                        {errorMessage}
                    </Alert>

                }
            </div>
            <Switch>
                <Route
                    exact
                    path='/'
                    render={(renderProps) => (
                        <DatasetReport {...others} {...renderProps} showErrorMessage={(msg)=>handleErrorMessage(msg)}>
                        </DatasetReport>
                    )}
                />
            </Switch>
            <Redirect to='/' />
        </div>
    )
}
export default injectIntl(Main)
