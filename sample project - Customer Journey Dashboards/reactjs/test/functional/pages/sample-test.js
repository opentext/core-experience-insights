import { Selector } from 'testcafe'
import { getPageURL } from '../shared/commonspecs/utils'

/**
 * Basic sample page tests.
 */

fixture `ui.Sample application tests`
    .page( getPageURL())
    .beforeEach( async t => {
        //await t.useRole(loginAction)
    })

test('Load Application', async t => {
    await t
      .expect(Selector('.otxec-main-page').exists).ok()
})
