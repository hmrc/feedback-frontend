package pages

import pages.behaviours.PageBehaviours


class WhatDidYouComeHereToDoPageSpec extends PageBehaviours {

  "WhatDidYouComeHereToDoPage" must {

    beRetrievable[String](WhatDidYouComeHereToDoPage)

    beSettable[String](WhatDidYouComeHereToDoPage)

    beRemovable[String](WhatDidYouComeHereToDoPage)
  }
}
