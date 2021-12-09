package com.dpsd.hamo.dbmodel.dbhelpers;

import java.util.ArrayList;

public interface GivingGetter
{
    public void processGivingsSuccess(ArrayList<Givings> givings);

    public void processFailure();
}
