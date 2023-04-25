import React from 'react'
import PropTypes from 'prop-types'
import { Alert } from 'reactstrap'

const Message = ({ text, type, dismissible }) => {
    return (
        <div className='message m-auto'>
            <Alert color={type} dismissible={dismissible}>{text}</Alert>
        </div>
    )
}

Message.propTypes = {
    text        : PropTypes.string,
    type        : PropTypes.string,
    dismissible : PropTypes.bool
}
export default Message
