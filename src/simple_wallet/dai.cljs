(ns simple-wallet.dai)

;;RinkebyDai Token
(def address "0x8ad3aa5d5ff084307d28c8f514d7a193b2bfe725") 
(def abi #js [
    ;; Read-Only Functions
    "function balanceOf(address owner) view returns (uint256)",
    "function decimals() view returns (uint8)",
    "function symbol() view returns (string)",

    ;; Authenticated Functions
    "function transfer(address to, uint amount) returns (boolean)",

    ;; Events
    "event Transfer(address indexed from, address indexed to, uint amount)"
              
    ;; contract specific functions
    "function mint(address to, uint256 amount) returns (boolean)"
])