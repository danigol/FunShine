package com.daniellegolinsky.funshine.models

import com.daniellegolinsky.funshine.api.RequestDatapoints

// I feel the need
// the need for speed!
// I'm not even a huge Top Gun fan. I haven't even seen the new one yet!
// But I do like going fast.
enum class SpeedUnit(unit: String) {
    MPH(RequestDatapoints.MPH),
    KPH(RequestDatapoints.KPH)
}