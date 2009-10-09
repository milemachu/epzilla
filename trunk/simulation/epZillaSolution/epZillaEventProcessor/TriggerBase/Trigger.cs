using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using epZillaShared;

namespace epZillaEventProcessor.TriggerBase
{
  public  class Trigger:ITrigger
    {

        #region ITrigger Members

        public DateTime RecievedTime
        {
            get
            {
                return this.RecievedTime;
            }
            set
            {
                this.RecievedTime = value;
            }
        }

        public string TriggerData
        {
            get
            {
                return this.TriggerData;
            }
            set
            {
                this.TriggerData = value;
            }
        }

        public string TriggerId
        {
            get
            {
                return this.TriggerId;
            }
            set
            {
                this.TriggerId = value;
            }
        }

        public string TriggerSource
        {
            get
            {
                return this.TriggerSource;
            }
            set
            {
                this.TriggerSource = value;
            }
        }

        #endregion



       
    }
}
